import Controller from '@ember/controller';
import { action } from '@ember/object';
import { tracked } from '@glimmer/tracking';
import { service } from '@ember/service';
export default class WriterEditerController extends Controller {
  @tracked notes;
  @service('data') data;
  @tracked today;
  @action
  tagToString(tags) {
    var element = document.createElement('p');
    element.innerHTML = tags;
    return element.innerHTML;
  }
  @action
  setToday() {
    const today = new Date();
    const yyyy = today.getFullYear();
    let mm = today.getMonth() + 1; // Months start at 0!
    let dd = today.getDate();
    if (dd < 10) dd = '0' + dd;
    if (mm < 10) mm = '0' + mm;
    const date = yyyy + '-' + mm + '-' + dd;
    return date;
  }
  @action
  save() {
    let data = document.getElementById('editable-content').innerHTML;
    let json = {
      userid: this.data.userId,
      type: 'update',
      data,
    };
    $.ajax({
      type: 'post',
      url: 'http://' + this.data.host + '/Diary/edit',
      data: JSON.stringify(json),
      success: (response) => {
        console.log(response + 'responses');
        console.log('saved syces');
        $('.save-status').toggleClass('save-status-toggle');
        setTimeout(() => {
          $('.save-status').toggleClass('save-status-toggle');
        }, 2000);
      },
    });
  }
  @action
  onLoad() {
    let today = this.setToday().split('-');
    this.today = today[2] + '-' + today[1] + '-' + today[0];
    let date = new Date();
    console.log('date ' + date);

    $.ajax({
      type: 'get',
      url: 'http://' + this.data.host + '/Diary/getnotes',
      data: 'userid=' + this.data.userId + '&date=' + this.today + '&type=edit',
      success: (response) => {
        let json = JSON.parse(response);
        console.log(response);
        this.data.notes = json;
        console.log('notes');
      },
      statusCode: {
        404: () => {
          console.log('no records');
        },
      },
    });
  }
}
