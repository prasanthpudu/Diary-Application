import Controller from '@ember/controller';
import { action } from '@ember/object';
import { tracked } from '@glimmer/tracking';
import { service } from '@ember/service';
export default class WriterViewController extends Controller {
  @service('data') data;
  @tracked date;
  @tracked notes;

  @action
  get() {
    let today = this.model.split('-');
    let actionType;
    this.date = today[2] + '-' + today[1] + '-' + today[0];
    console.log(this.data.today + '' + this.model + 'acitontype cheing');
    if (this.data.today == this.model) {
      actionType = 'edit';
    } else {
      actionType = 'view';
    }
    console.log('happening');
    let type = 'get';
    let url = this.data.domain + '/search';
    let data =
      'type=getnotes&userid=' +
      this.data.userId +
      '&date=' +
      this.date +
      '&actiontype=' +
      actionType;
    let processData = true;
    let contentType;
    this.data
      .ajax(type, url, data, processData, contentType, true)
      .then((response) => {
        let json = JSON.parse(response);
        this.data.notes = json;
        console.log(this.data.newNote);
      });
  }
}
