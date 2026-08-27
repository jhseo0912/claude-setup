# claude-setup

Claude Code를 쓰는 사람이 자기 계정에 한 번 깔아두는 전역 설정입니다.
말투 규칙, md가 일기장이 되는 것을 막는 hook, 반복 작업 스킬이 들어 있습니다.
새 기기나 새 계정에서 `bash install.sh` 한 번이면 그대로 돌아옵니다.

## 설치

Claude Code가 설치돼 있어야 하고 hook 등록에 node를 씁니다.

```bash
git clone https://github.com/jhseo0912/claude-setup.git
cd claude-setup
bash install.sh
```

`~/.claude/`에 파일을 복사하고 settings.json에 hook을 등록합니다.
기존 `~/.claude/CLAUDE.md`는 `.bak`으로 백업합니다. settings.json은 hook만 추가하고 나머지 설정은 그대로 둡니다.
새 Claude Code 세션부터 적용됩니다.

## 사용

설치하면 모든 프로젝트에 전역 규칙이 적용되고, 스킬을 이름으로 부를 수 있습니다.

| 스킬 | 언제 | 하는 일 |
|---|---|---|
| `/commit` | 커밋할 때 | diff만 본 haiku 서브에이전트가 커밋 메시지를 씁니다. 세션 맥락이 섞이지 않아 장황해지지 않습니다 |
| `/wrap` | 작업을 끝낼 때 | 세션이 남긴 것을 결과물·지식·부산물로 나눠, 지식은 문서로 옮기고 부산물은 지운 뒤 커밋합니다 |
| `/distill` | md가 비대해졌을 때 | 늘어진 기록을 제약·결정(ADR)·상태·docs 네 갈래로 추려냅니다 |
| `/humanize-korean` | 한국어 글의 AI 문체를 걷어낼 때 | 번역투와 기계적 병렬 구조를 찾아 고쳐 씁니다 |

`/humanize-korean`은 [epoko77-ai/im-not-ai](https://github.com/epoko77-ai/im-not-ai)에서 만든 스킬입니다(MIT).
install.sh가 `~/.claude/vendor/im-not-ai`에 내려받아 함께 깔아줍니다.

## 구성

| 파일 | 역할 |
|---|---|
| `CLAUDE.md` | 전역 규칙. 대화 형식(두괄식·개조식), 말과 일, 문서 압축 규칙 |
| `hooks/md-discipline.sh` | 지식 md를 고칠 때 압축 규칙을 다시 알려주는 PostToolUse hook |
| `merge-hooks.js` | settings.json에 위 hook을 병합합니다. 이미 있으면 건드리지 않습니다 |
| `skills/commit/SKILL.md` | 커밋 메시지 스킬 |
| `skills/wrap/SKILL.md` | 세션 마무리 스킬 |
| `skills/distill/SKILL.md` | md 추려내기 스킬 |
| `install.sh` | 위 파일을 `~/.claude/`에 복사하고 hook을 등록합니다 |

## 제거

```bash
rm -rf ~/.claude/skills/{commit,wrap,distill} ~/.claude/hooks/md-discipline.sh ~/.claude/CLAUDE.md
bash ~/.claude/vendor/im-not-ai/uninstall.sh && rm -rf ~/.claude/vendor/im-not-ai
```

설치 전에 쓰던 전역 규칙은 `~/.claude/CLAUDE.md.bak`에 남아 있습니다. 이름만 바꾸면 돌아옵니다.
settings.json은 자동으로 되돌리지 않습니다. `hooks.PostToolUse`에서 `md-discipline.sh`를 부르는 항목 하나를 지우면 됩니다.

## 수정

원본은 이 리포입니다. `~/.claude/`를 직접 고쳤으면 여기로 복사해 커밋하세요. 두 곳에 두면 갈립니다.
고친 뒤 `bash install.sh`를 다시 돌리면 반영됩니다.
