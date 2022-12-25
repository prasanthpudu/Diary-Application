import Route from '@ember/routing/route';
import { service } from '@ember/service';
export default class RegisterRoute extends Route {
  @service router;
  @service('data') data;
  beforeModel() {
    if (!this.data.tempId) {
      this.router.transitionTo('login');
    }
  }
}
