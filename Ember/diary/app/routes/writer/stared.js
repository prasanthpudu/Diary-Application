import Route from '@ember/routing/route';
import { service } from '@ember/service';
export default class WriterStaredRoute extends Route {
  @service('data') data;
  model() {
    this.data.page = 'stared';
  }
}
