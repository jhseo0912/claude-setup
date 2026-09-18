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
    // compact 뒤 주입은 SessionStart matcher compact다. PostCompact는 스키마에 없어 거부된다.
    event: 'SessionStart',
    matcher: 'compact',
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

// 검색 위임 규칙이 부르는 cavecrew-investigator는 caveman 플러그인 소속이다.
// 마켓플레이스 등록과 활성화를 여기서 같이 해준다. 이미 있으면 안 건드린다.
s.extraKnownMarketplaces = s.extraKnownMarketplaces || {};
if (!s.extraKnownMarketplaces.caveman) {
  s.extraKnownMarketplaces.caveman = { source: { source: 'github', repo: 'juliusbrussee/caveman' } };
  changed = true;
  console.log('caveman marketplace 등록');
} else {
  console.log('caveman marketplace 이미 등록됨, 변경 없음');
}

s.enabledPlugins = s.enabledPlugins || {};
if (!('caveman@caveman' in s.enabledPlugins)) {
  s.enabledPlugins['caveman@caveman'] = true;
  changed = true;
  console.log('caveman 플러그인 활성화');
} else {
  console.log('caveman 플러그인 설정 이미 있음, 변경 없음');
}

if (changed) fs.writeFileSync(path, JSON.stringify(s, null, 2) + '\n');
