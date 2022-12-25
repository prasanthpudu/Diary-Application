import Route from '@ember/routing/route';
import { service } from '@ember/service';
export default class WriterViewRoute extends Route {
  @service('data') data;
  model(params) {
    this.data.page = 'view';
    let { date } = params;
    return date;
  }
}
