# supervisor

실행 중인 Claude Code 세션이 컨텍스트를 얼마나 물고 있는지 보여주는 대시보드입니다.
세션이 부풀면 매 턴 그만큼을 캐시로 다시 읽어 비용이 붙는데, 지금은 그 크기를 볼 방법이 없습니다.
v0.1은 조회와 계측까지 하고, 압축 자동화는 아직 없습니다.

## 실행

```bash
cd supervisor
npm install
npm start
```

- 필요한 것: node, `claude` CLI
- 주소는 http://localhost:4477
- 포트는 `PORT`, 경고 임계는 `WARN_TOKENS` 환경변수로 바꿉니다 (기본 30만 토큰)

## 보이는 것

| 열 | 내용 |
|---|---|
| 세션 | 세션 이름과 짧은 id |
| 종류 | interactive, background, blocked |
| 컨텍스트 | 현재 컨텍스트 토큰. 임계 초과 시 주황색 |
| 트랜스크립트 | 기록 파일 크기 |
| 작업 폴더 | 세션의 cwd |
| 등록 | 관리 대상 표시. `sessions.json`에 저장됩니다 |

## 컨텍스트를 재는 방법

컨텍스트는 파일이 아니라 세션 프로세스의 메모리에 있어 직접 못 잽니다.
대신 트랜스크립트 마지막 API 호출의 `cache_read_input_tokens`와
`cache_creation_input_tokens` 합이 그 시점의 컨텍스트와 같습니다. 그 값을 읽습니다.

## 구성

| 파일 | 역할 |
|---|---|
| `server.js` | `claude agents --json` 조회, 등록 API, 정적 파일 제공 |
| `context.js` | 트랜스크립트를 끝에서부터 읽어 컨텍스트 토큰 산출 |
| `public/index.html` | 대시보드 화면 |

## 아직 안 되는 것

- background 세션은 트랜스크립트를 못 찾습니다. 경로 규칙이 다른 것으로 보입니다
- 압축 자동화가 없습니다. 세션의 컨텍스트를 줄이려면 그 세션을 쓰는 프로세스 안에서
  `/compact`가 실행되어야 하는데, 다른 프로세스에서 같은 대화를 열어 압축해도
  원래 프로세스의 메모리는 그대로입니다. supervisor가 그 프로세스의 입력을 쥐어야
  하고, 그러려면 세션을 supervisor가 다시 띄우는 인수인계가 필요합니다
