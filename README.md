# claude-setup

Claude Code 전역 설정입니다. 새 기기·새 계정에서 `bash install.sh` 한 번이면 그대로 돌아옵니다.

- CLAUDE.md에 담긴 말투 규칙
- md가 일기장이 되는 것을 막는 hook
- 리포에 든 스킬 3종
- 함께 깔리는 외부 스킬 1종

## 설치

```bash
git clone https://github.com/jhseo0912/claude-setup.git
cd claude-setup
bash install.sh
```

- 필요한 것: Claude Code, 그리고 hook 등록에 쓸 node
- `~/.claude/`에 파일 복사 + settings.json에 hook 등록
- 기존 `~/.claude/CLAUDE.md`는 `.bak`으로 백업
- settings.json은 hook만 추가, 나머지 설정은 그대로
- 새 Claude Code 세션부터 적용

## 사용

전역 규칙은 모든 프로젝트에 자동으로 붙고, 스킬은 이름으로 부릅니다.

| 스킬 | 언제 | 하는 일 |
|---|---|---|
| `/commit` | 커밋할 때 | diff만 본 haiku 서브에이전트가 메시지 작성. 세션 맥락이 안 섞여 장황해지지 않음 |
| `/wrap` | 작업을 끝낼 때 | 남은 것을 결과물·지식·부산물로 나눠 지식은 문서로, 부산물은 삭제 후 커밋 |
| `/distill` | md가 비대해졌을 때 | 늘어진 기록을 제약·결정·상태·docs 네 갈래로 추려냄 |
| `/humanize-korean` | 한국어 글의 AI 문체를 걷어낼 때 | 번역투·기계적 병렬 구조를 찾아 교정 |

## 구성

| 파일 | 역할 |
|---|---|
| `CLAUDE.md` | 전역 규칙. 대화 형식, 말과 일, 문서 압축 |
| `hooks/md-discipline.sh` | 지식 md를 고칠 때 압축 규칙을 다시 알려주는 PostToolUse hook |
| `merge-hooks.js` | settings.json에 위 hook 병합. 이미 있으면 건드리지 않음 |
| `skills/commit/SKILL.md` | 커밋 메시지 스킬 |
| `skills/wrap/SKILL.md` | 세션 마무리 스킬 |
| `skills/distill/SKILL.md` | md 추려내기 스킬 |
| `install.sh` | 위 파일을 `~/.claude/`에 복사하고 hook 등록 |

## 외부 의존

| 패키지 | 라이선스 | 받는 곳 | 쓰는 데 |
|---|---|---|---|
| [im-not-ai](https://github.com/epoko77-ai/im-not-ai) | MIT | `~/.claude/vendor/im-not-ai` | `/humanize-korean` 스킬 |

- install.sh가 클론해서 함께 설치
- git·네트워크가 없으면 이 단계만 건너뛰고 나머지는 정상 완료

## 제거

```bash
rm -rf ~/.claude/skills/{commit,wrap,distill} ~/.claude/hooks/md-discipline.sh ~/.claude/CLAUDE.md
bash ~/.claude/vendor/im-not-ai/uninstall.sh && rm -rf ~/.claude/vendor/im-not-ai
```

- 설치 전 전역 규칙은 `~/.claude/CLAUDE.md.bak`에 보존. 이름만 바꾸면 복구
- settings.json은 수동 정리. `hooks.PostToolUse`에서 `md-discipline.sh` 항목 하나만 삭제

## 수정

- 원본은 이 리포. `~/.claude/`를 직접 고쳤으면 여기로 복사해 커밋
- 두 곳에 두면 갈림
- 고친 뒤 `bash install.sh` 재실행하면 반영
