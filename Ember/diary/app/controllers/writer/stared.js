import Controller from '@ember/controller';
import { action } from '@ember/object';
import { service } from '@ember/service';
export default class WriterStaredController extends Controller {
  @service('data') data;
  @action
  onLoad() {
    let type = 'get';
    let url = this.data.domain + '/search';
    let data = 'type=stared&userid=' + this.data.userId;
    let processData = true;
    let contentType;
    this.data
      .ajax(type, url, data, processData, contentType, true)
      .then((response) => {
        this.data.notes = JSON.parse(response);
      });
  }
}
