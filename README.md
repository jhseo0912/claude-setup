# claude-setup

Claude Code를 쓰는 사람이 자기 계정에 한 번 깔아두는 전역 설정.
말투 규칙, md가 일기장이 되는 것을 막는 hook, 반복 작업 스킬이 들어 있다.
새 기기나 새 계정에서 `bash install.sh` 한 번이면 그대로 돌아온다.

## 설치

Claude Code가 설치돼 있어야 하고 hook 등록에 node를 쓴다.

```bash
git clone https://github.com/jhseo0912/claude-setup.git
cd claude-setup
bash install.sh
```

`~/.claude/`에 파일을 복사하고 settings.json에 hook을 등록한다.
기존 `~/.claude/CLAUDE.md`는 `.bak`으로 백업된다. settings.json은 hook만 추가되고 나머지 설정은 유지된다.
새 Claude Code 세션부터 적용된다.

## 사용

설치하면 모든 프로젝트에서 전역 규칙이 적용되고 스킬을 이름으로 부를 수 있다.

| 스킬 | 언제 | 하는 일 |
|---|---|---|
| `/commit` | 커밋할 때 | diff만 본 haiku 서브에이전트가 커밋 메시지를 쓴다. 세션 맥락이 섞이지 않아 장황해지지 않는다 |
| `/wrap` | 작업을 끝낼 때 | 세션이 남긴 것을 결과물·지식·부산물로 갈라 지식은 문서로 옮기고 부산물은 지운 뒤 커밋한다 |
| `/distill` | md가 비대해졌을 때 | 늘어진 기록을 제약·결정(ADR)·상태·docs 네 갈래로 추려낸다 |
| `/humanize-korean` | 한국어 글의 AI 문체를 걷어낼 때 | 번역투와 기계적 병렬 구조를 찾아 고쳐 쓴다 |

## 구성

| 파일 | 역할 |
|---|---|
| `CLAUDE.md` | 전역 규칙. 대화 형식(두괄식·개조식), 말과 일, 문서 압축 규칙 |
| `hooks/md-discipline.sh` | 지식 md를 고칠 때 압축 규칙을 다시 알려주는 PostToolUse hook |
| `merge-hooks.js` | settings.json에 위 hook을 병합한다. 이미 있으면 건드리지 않는다 |
| `skills/commit/SKILL.md` | 커밋 메시지 스킬 |
| `skills/wrap/SKILL.md` | 세션 마무리 스킬 |
| `skills/distill/SKILL.md` | md 증류 스킬 |
| `install.sh` | 위 파일을 `~/.claude/`에 복사하고 hook을 등록한다 |

`/humanize-korean`은 이 리포에 없다. install.sh가 [epoko77-ai/im-not-ai](https://github.com/epoko77-ai/im-not-ai)(MIT)를
`~/.claude/vendor/im-not-ai`로 클론해 그쪽 설치 스크립트에 맡긴다. 원본은 저쪽에 있다.

## 제거

```bash
rm -rf ~/.claude/skills/{commit,wrap,distill} ~/.claude/hooks/md-discipline.sh ~/.claude/CLAUDE.md
bash ~/.claude/vendor/im-not-ai/uninstall.sh && rm -rf ~/.claude/vendor/im-not-ai
```

설치 전에 쓰던 전역 규칙이 있었다면 `~/.claude/CLAUDE.md.bak`에 남아 있다. 되돌리려면 이름만 바꾸면 된다.
settings.json은 자동으로 되돌리지 않는다. `hooks.PostToolUse`에서 `md-discipline.sh`를 부르는 항목 하나를 지우면 된다.

## 수정

원본은 이 리포다. `~/.claude/`를 직접 고쳤으면 여기로 복사해 커밋한다. 두 곳에 두면 갈린다.
고친 뒤 `bash install.sh`를 다시 돌리면 반영된다.
