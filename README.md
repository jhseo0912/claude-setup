# claude-setup

Claude Code 전역 설정. 전역 규칙(CLAUDE.md) + md 규율 hook + /distill 스킬.
새 기기·계정에서 `bash install.sh` 한 번이면 끝.

## 구성

| 파일 | 역할 |
|---|---|
| `CLAUDE.md` | 전역 규칙: 대화 형식(두괄식·개조식), 말과 일, 문서 압축 규범 |
| `hooks/md-discipline.sh` | 지식 md(CLAUDE.md·memory/·docs/) 수정 시 압축 규칙 리마인더를 주입하는 PostToolUse hook |
| `skills/distill/SKILL.md` | 비대한 md를 제약/ADR/상태/docs 4갈래로 증류하는 스킬 |
| `install.sh` | 위 파일을 `~/.claude/`에 복사하고 settings.json에 hook 병합 |

## 설치

```bash
git clone https://github.com/jhseo0912/claude-setup.git
cd claude-setup
bash install.sh
```

기존 `~/.claude/CLAUDE.md`는 `.bak`으로 백업된다. settings.json은 hook만 추가되고 나머지는 그대로.

## 수정

정본은 이 리포. `~/.claude/`를 직접 고쳤으면 여기로 복사해 커밋한다. 두 곳에 두면 갈린다.
