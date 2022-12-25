import Route from '@ember/routing/route';
import { service } from '@ember/service';
import { action } from '@ember/object';
export default class WriterRoute extends Route {
  @service router;
  @service('data') data;
  beforeModel() {
    if (!this.data.userId) {
      console.log(this.data.userId);
      console.log("from writer route");
      this.router.transitionTo('login');
    }
    else{
      console.log("this data today "+this.data.today);
      this.router.transitionTo('writer.view',this.data.today);
    }
  }
}
