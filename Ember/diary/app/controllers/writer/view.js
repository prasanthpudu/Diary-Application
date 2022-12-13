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
    this.date = today[2] + '-' + today[1] + '-' + today[0];
    console.log('date paraem');
    console.log(this.model);
    let date = new Date(this.model);
    console.log('date ' + date);
    let url =
      'userid=' + this.data.userId + '&date=' + this.date + '&type=view';
    $.ajax({
      type: 'get',
      url: 'http://' + this.data.host + '/Diary/getnotes',
      data: url,
      success: (response) => {
        console.log(response);
        let json = JSON.parse(response);
        this.data.notes = json;
      },
    });
  }
}
