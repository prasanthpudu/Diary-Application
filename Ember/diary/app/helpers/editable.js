import { helper } from '@ember/component/helper';
export default helper(function editable([date, today]) {
  let format = date.substring(0, 10);

  if (format == today) {
    return true;
  }
  return false;
});
