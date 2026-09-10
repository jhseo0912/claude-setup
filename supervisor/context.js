// 세션 하나의 현재 컨텍스트 크기를 트랜스크립트에서 읽어낸다.
// 컨텍스트는 프로세스 메모리에 있어 직접 못 재지만, 마지막 API 호출의
// cache_read + cache_creation 합이 그 시점 컨텍스트와 같다.
const fs = require('fs');
const path = require('path');
const os = require('os');

const PROJECTS = path.join(os.homedir(), '.claude', 'projects');
const TAIL_BYTES = 400 * 1024;

function findTranscript(sessionId) {
  let dirs;
  try {
    dirs = fs.readdirSync(PROJECTS);
  } catch {
    return null;
  }
  for (const d of dirs) {
    const p = path.join(PROJECTS, d, `${sessionId}.jsonl`);
    if (fs.existsSync(p)) return p;
  }
  return null;
}

// 파일 끝에서부터 usage가 붙은 마지막 assistant 레코드를 찾는다.
function readLastUsage(file) {
  let fd;
  try {
    fd = fs.openSync(file, 'r');
    const size = fs.fstatSync(fd).size;
    const len = Math.min(TAIL_BYTES, size);
    const buf = Buffer.alloc(len);
    fs.readSync(fd, buf, 0, len, size - len);
    const lines = buf.toString('utf8').split('\n');
    for (let i = lines.length - 1; i >= 0; i--) {
      const line = lines[i];
      if (!line.includes('"usage"')) continue;
      let d;
      try {
        d = JSON.parse(line);
      } catch {
        continue;
      }
      const u = d?.message?.usage;
      if (!u) continue;
      return {
        contextTokens:
          (u.cache_read_input_tokens || 0) +
          (u.cache_creation_input_tokens || 0) +
          (u.input_tokens || 0),
        outputTokens: u.output_tokens || 0,
        at: d.timestamp || null,
      };
    }
    return null;
  } catch {
    return null;
  } finally {
    if (fd !== undefined) fs.closeSync(fd);
  }
}

function measure(sessionId) {
  const file = findTranscript(sessionId);
  if (!file) return { transcript: null, contextTokens: null };
  const u = readLastUsage(file);
  return {
    transcript: file,
    sizeBytes: fs.statSync(file).size,
    contextTokens: u ? u.contextTokens : null,
    lastActivity: u ? u.at : null,
  };
}

module.exports = { measure, findTranscript };
