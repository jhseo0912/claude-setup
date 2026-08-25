// settings.json에 md-discipline hook을 병합한다. 이미 있으면 건드리지 않는다.
const fs = require('fs');
const path = process.argv[2];
if (!path) { console.error('사용법: node merge-hooks.js <settings.json 경로>'); process.exit(1); }

const s = fs.existsSync(path) ? JSON.parse(fs.readFileSync(path, 'utf8')) : {};
const cmd = 'bash "$HOME/.claude/hooks/md-discipline.sh"';

s.hooks = s.hooks || {};
s.hooks.PostToolUse = s.hooks.PostToolUse || [];
const exists = s.hooks.PostToolUse.some(m => (m.hooks || []).some(h => h.command === cmd));

if (exists) {
  console.log('hook 이미 등록됨, 변경 없음');
} else {
  s.hooks.PostToolUse.push({
    matcher: 'Write|Edit',
    hooks: [{ type: 'command', command: cmd, timeout: 10 }],
  });
  fs.writeFileSync(path, JSON.stringify(s, null, 2) + '\n');
  console.log('hook 등록 완료: ' + path);
}
