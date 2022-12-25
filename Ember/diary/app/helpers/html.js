import { helper } from '@ember/component/helper';
import Ember from 'ember';
export default helper(function html([param] /*, named*/) {
  return Ember.String.htmlSafe(`${param}`);
});
