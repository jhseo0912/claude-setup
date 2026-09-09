// settings.json에 전역 hook들을 병합한다. 이미 있으면 건드리지 않는다.
const fs = require('fs');
const path = process.argv[2];
if (!path) { console.error('사용법: node merge-hooks.js <settings.json 경로>'); process.exit(1); }

const s = fs.existsSync(path) ? JSON.parse(fs.readFileSync(path, 'utf8')) : {};
s.hooks = s.hooks || {};

const HOOKS = [
  {
    event: 'PostToolUse',
    matcher: 'Write|Edit',
    cmd: 'bash "$HOME/.claude/hooks/md-discipline.sh"',
  },
  {
    event: 'UserPromptSubmit',
    matcher: null,
    cmd: 'bash "$HOME/.claude/hooks/style-reminder.sh"',
  },
  {
    event: 'PostCompact',
    matcher: null,
    cmd: 'bash "$HOME/.claude/hooks/post-compact.sh"',
  },
];

let changed = false;
for (const { event, matcher, cmd } of HOOKS) {
  s.hooks[event] = s.hooks[event] || [];
  const exists = s.hooks[event].some(m => (m.hooks || []).some(h => h.command === cmd));
  if (exists) { console.log(`${event} hook 이미 등록됨, 변경 없음`); continue; }
  const entry = { hooks: [{ type: 'command', command: cmd, timeout: 10 }] };
  if (matcher) entry.matcher = matcher;
  s.hooks[event].push(entry);
  changed = true;
  console.log(`${event} hook 등록: ${cmd}`);
}

if (changed) fs.writeFileSync(path, JSON.stringify(s, null, 2) + '\n');
