import Route from '@ember/routing/route';
import { service } from '@ember/service';
import {action } from '@ember/object';
export default class WriterRoute extends Route {
  @service router;
  @service('data') data;
  beforeModel() {
    let today = new Date().getTime();
    today = this.setDate(today);
    $.ajax({
      type: 'get',
      url: 'http://' + this.data.host + '/Diary/getsession',
      xhrFields: {
        withCredentials: true,
      },
      success: (response) => {
        console.log('resonponing');
        let data = JSON.parse(response);
        this.data.userId = data.userId;
        console.log('userId' + data.userId);
        this.router.transitionTo('writer.view',today);
      },
      statusCode: {
        404: () => {
          console.log('exiedt');
          this.router.transitionTo('login');
        },
      },
    });
  }
  @action
  setDate(millseconds) {
    const today = new Date(millseconds);
    const yyyy = today.getFullYear();
    let mm = today.getMonth() + 1; // Months start at 0!
    let dd = today.getDate();
    if (dd < 10) dd = '0' + dd;
    if (mm < 10) mm = '0' + mm;
    const date = yyyy + '-' + mm + '-' + dd;
    return date;
  }
}
