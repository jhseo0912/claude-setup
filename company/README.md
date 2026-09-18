# company

세션 여러 개를 회사처럼 나눠 일하는 운영 규칙입니다. JESC2에서 2026-09 한 주 동안 검증한 것을 프로젝트 이름과 팀 내용을 뺀 사본으로 옮겼습니다.
역할이 곧 스킬입니다. 세션은 자기 역할 스킬 하나를 읽고 일합니다. 공유 문서는 company 스킬의 템플릿에서 프로젝트로 복사합니다.

## 구성
| 자리 | 내용 |
|---|---|
| skills/company | `/company init`. 프로젝트에 company-docs 뼈대와 CLAUDE.md 절을 복사 |
| skills/company/templates | org.md(직급표, 운영 규칙), session-protocol.md(카드 형식), roster.md, meeting-logs, roles, ADR |
| skills/role-group-lead | 그룹장. 큰 결정, 검토, 정기보고회의, 주간 마무리, 대표 보고 |
| skills/role-hr | HR. 역할 파일, 명부, 온보딩, 점호, 전파, 카드 규율 |
| skills/role-pm | PM. 커밋 창, 숫자 측정, docs 정리 |
| skills/role-secretary | 비서. Haiku, 메뉴 실행만 |
| skills/role-pipeline | 파이프라인팀장. 선택 역할. 관찰, 재현 검증, 제안 |
| skills/role-lead | 팀장 공통 |
| skills/role-member | 팀원 공통과 Haiku 서브에이전트 사용 |
| hooks/post-compact.sh | compact 뒤 자기 주소 확인과 HR [PING-SELF] |

## 설치
루트 install.sh가 skills/를 ~/.claude/skills/로, hooks/를 ~/.claude/hooks/로 복사하고 compact 복구 훅을 SessionStart matcher compact로 병합합니다.

## 프로젝트에서 시작
1. 프로젝트 폴더에서 `/company init <프로젝트 이름>`. company-docs/가 생기고 CLAUDE.md에 절이 붙습니다
2. org.md 조직도, 팀 이름, 소유 경로 표를 채웁니다
3. 팀마다 company-docs/roles/<팀>/lead.md를 씁니다. role-lead 위에 얹는 프로젝트 직무만
4. 세션을 만들고 첫 메시지를 줍니다. "너는 <프로젝트> <역할>이다. /role-<역할> 을 읽고 HR에게 연락하라"

## 프로젝트에 남는 것과 여기 있는 것
| 여기(일반) | 프로젝트(고유) |
|---|---|
| 직급별 허용범위, 운영 규칙, 카드 형식, 회의와 마무리 절차 | 조직도, 팀 이름, 소유 경로, 관문 |
| 스태프 넷 직무, 팀장과 팀원 공통 직무 | roles/<팀>/lead.md, 방법서, roster 값 |
| 기본값. 회의 4시간, 카드 상한, 커밋 창 15분 | 기본값을 바꾸면 org.md에 덮어씀 |
