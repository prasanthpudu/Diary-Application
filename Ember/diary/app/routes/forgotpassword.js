import Route from '@ember/routing/route';
import { service } from '@ember/service';
export default class ForgotpasswordRoute extends Route {
  @service router;
  @service('data') data;
  beforeModel() {
    if (!this.data.tempId) {
      this.router.transitionTo('login');
    }
  }
}
