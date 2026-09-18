# claude-setup

Claude Code 전역 설정입니다. 새 기기나 새 계정에서 `bash install.sh` 한 번이면 그대로 돌아옵니다.

- CLAUDE.md에 담긴 말투 규칙
- md가 일기장이 되는 것을 막는 hook
- 리포에 든 스킬 5종
- 세션 여러 개를 회사처럼 나눠 일하는 운영 규칙 company/
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
| `/commit` | 커밋할 때 | diff만 본 haiku가 커밋 메시지 작성 |
| `/wrap` | 작업을 끝낼 때 | 지식은 문서로 옮기고 부산물은 지운 뒤 커밋 |
| `/distill` | md가 비대해졌을 때 | 늘어진 기록을 네 갈래로 추려냄 |
| `/readme` | README가 코드와 어긋날 때 | 갱신 후 haiku에게 읽혀 검증 |
| `/release` | 태그를 끊을 때 | 깨지는 변경을 앞세운 릴리스 노트 |
| `/humanize-korean` | 한국어 글이 AI 같을 때 | 번역투와 기계적 병렬 구조 교정 |
| `/company init` | 세션 조직으로 시작할 때 | company-docs 뼈대 복사 |
| `/role-<역할>` | 세션 온보딩 때 | 그룹장, HR, PM, 비서, 파이프라인, 팀장, 팀원 직무 |

## 구성

| 파일 | 역할 |
|---|---|
| `CLAUDE.md` | 대화 형식, 일하는 순서, 문서 규칙 |
| `hooks/md-discipline.sh` | 지식 md를 고칠 때 압축 규칙을 다시 알려줌 |
| `hooks/style-reminder.sh` | 매 턴 문체와 검색 위임 규칙을 다시 알려줌 |
| `merge-hooks.js` | settings.json에 위 hook 병합 |
| `skills/commit/SKILL.md` | 커밋 메시지 스킬 |
| `skills/wrap/SKILL.md` | 세션 마무리 스킬 |
| `skills/distill/SKILL.md` | md 추려내기 스킬 |
| `skills/readme/SKILL.md` | README 작성 스킬 |
| `skills/release/SKILL.md` | 릴리스 노트 스킬 |
| `company/` | 역할 스킬 일곱과 company 템플릿, compact 복구 hook |
| `supervisor/` | 실행 중 세션의 컨텍스트 크기를 보는 대시보드. 상세는 supervisor/README.md |
| `install.sh` | 위 파일 복사 + hook 등록 |

## 외부 의존

| 패키지 | 라이선스 | 받는 곳 | 쓰는 데 |
|---|---|---|---|
| [im-not-ai](https://github.com/epoko77-ai/im-not-ai) | MIT | `~/.claude/vendor/im-not-ai` | `/humanize-korean` 스킬 |
| [caveman](https://github.com/juliusbrussee/caveman) | MIT | 플러그인 마켓플레이스 | 검색 위임 규칙의 `cavecrew-investigator` |

- im-not-ai는 install.sh가 클론해서 함께 설치. git이나 네트워크가 없으면 이 단계만 건너뛰고 나머지는 정상 완료
- caveman은 install.sh가 settings.json에 마켓플레이스 등록과 플러그인 활성화만 해준다. 실제 설치는 다음 세션 시작 때 Claude Code가 받는다
- caveman 플러그인이 없으면 검색 위임 규칙이 못 돈다. CLAUDE.md 규칙대로 Explore로 대신하지 않고 멈춘다

## 제거

```bash
rm -rf ~/.claude/skills/{commit,wrap,distill,readme,release}
rm -rf ~/.claude/skills/{company,role-group-lead,role-hr,role-pm,role-secretary,role-pipeline,role-lead,role-member}
rm -f ~/.claude/hooks/post-compact.sh
rm -f ~/.claude/hooks/md-discipline.sh ~/.claude/hooks/style-reminder.sh ~/.claude/CLAUDE.md
bash ~/.claude/vendor/im-not-ai/uninstall.sh
rm -rf ~/.claude/vendor/im-not-ai
```

- 설치 전 전역 규칙은 `~/.claude/CLAUDE.md.bak`에 보존. 이름만 바꾸면 복구
- settings.json은 수동 정리. `hooks.PostToolUse`에서 `md-discipline.sh` 항목 하나만 삭제

## 수정

- 원본은 이 리포. `~/.claude/`를 직접 고쳤으면 여기로 복사해 커밋
- 두 곳에 두면 갈림
- 고친 뒤 `bash install.sh` 재실행하면 반영
