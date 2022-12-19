import Route from '@ember/routing/route';
import { service } from '@ember/service';
import { action } from '@ember/object';
export default class WriterRoute extends Route {
  @service router;
  @service('data') data;
  async beforeModel() {
    let today = new Date().getTime();
    today = this.setDate(today);
    let type = 'get';
    let url = this.data.domain + '/session';
    let data = 'type=getsession';
    let processData = true;
    let contentType;
    let response = await this.data.ajax(
      type,
      url,
      data,
      processData,
      contentType,
      true
    );
    this.data.userId = JSON.parse(response).userId;
   
    this.router.transitionTo('writer.view',today);
    console.log(this.data.userId + 'from sevice');

    return;
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
