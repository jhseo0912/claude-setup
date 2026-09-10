const express = require('express');
const { execFile } = require('child_process');
const fs = require('fs');
const path = require('path');
const os = require('os');
const { measure } = require('./context');

const PORT = Number(process.env.PORT || 4477);
const REGISTRY = path.join(__dirname, 'sessions.json');
const CLAUDE = path.join(os.homedir(), '.local', 'bin', 'claude.exe');
const WARN_TOKENS = Number(process.env.WARN_TOKENS || 300000);

function loadRegistry() {
  try {
    return JSON.parse(fs.readFileSync(REGISTRY, 'utf8'));
  } catch {
    return { registered: [] };
  }
}

function saveRegistry(r) {
  fs.writeFileSync(REGISTRY, JSON.stringify(r, null, 2) + '\n');
}

function listAgents() {
  return new Promise((resolve) => {
    execFile(CLAUDE, ['agents', '--json'], { maxBuffer: 8 << 20 }, (err, stdout) => {
      if (err) return resolve([]);
      try {
        resolve(JSON.parse(stdout));
      } catch {
        resolve([]);
      }
    });
  });
}

const app = express();
app.use(express.json());
app.use(express.static(path.join(__dirname, 'public')));

app.get('/api/sessions', async (req, res) => {
  const agents = await listAgents();
  const reg = loadRegistry();
  const registered = new Set(reg.registered.map((r) => r.sessionId));
  const rows = agents.map((a) => {
    const m = measure(a.sessionId);
    const tokens = m.contextTokens;
    return {
      sessionId: a.sessionId,
      shortId: a.id || a.sessionId.slice(0, 8),
      name: a.name || null,
      cwd: a.cwd,
      kind: a.kind,
      state: a.state || (a.kind === 'interactive' ? 'active' : null),
      pid: a.pid || null,
      startedAt: a.startedAt ? Number(a.startedAt) : null,
      registered: registered.has(a.sessionId),
      contextTokens: tokens,
      over: tokens != null && tokens >= WARN_TOKENS,
      transcriptMB: m.sizeBytes ? +(m.sizeBytes / 1e6).toFixed(1) : null,
    };
  });
  rows.sort((a, b) => (b.contextTokens || 0) - (a.contextTokens || 0));
  res.json({ warnTokens: WARN_TOKENS, sessions: rows });
});

app.post('/api/register', (req, res) => {
  const { sessionId, name } = req.body || {};
  if (!sessionId) return res.status(400).json({ error: 'sessionId required' });
  const reg = loadRegistry();
  if (!reg.registered.some((r) => r.sessionId === sessionId)) {
    reg.registered.push({ sessionId, name: name || null, addedAt: Date.now() });
    saveRegistry(reg);
  }
  res.json({ ok: true });
});

app.post('/api/unregister', (req, res) => {
  const { sessionId } = req.body || {};
  const reg = loadRegistry();
  reg.registered = reg.registered.filter((r) => r.sessionId !== sessionId);
  saveRegistry(reg);
  res.json({ ok: true });
});

app.listen(PORT, () => {
  console.log(`supervisor http://localhost:${PORT}  (경고 임계 ${WARN_TOKENS.toLocaleString()} 토큰)`);
});
