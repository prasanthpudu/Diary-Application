import { helper } from '@ember/component/helper';

export default helper(function substring([string, start, end] /*, named*/) {
  return string.substring(start, end);
});
