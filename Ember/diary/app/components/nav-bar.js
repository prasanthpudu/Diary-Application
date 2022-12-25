import Component from '@glimmer/component';
import { service } from '@ember/service';
import { action } from '@ember/object';
import { tracked } from '@glimmer/tracking';
import { A } from '@ember/array';
import Route from '@ember/routing/route';
export default class NavBarComponent extends Component {
  @service('data') data;
  @tracked lastTwoDates;
  @service router;
  today;

  yesterday;
  filters = false;
  @action
  toggle() {
    $('.quick-links').toggleClass('quick-drop');
    if (this.filters) $('#view-link').css('margin-top', '-285px');
    else $('#view-link').css('margin-top', '0px');
    this.filters = !this.filters;
  }
  host = 'localhost:8080';
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
  @action
  setToday() {
    let today = new Date().getTime();
    this.today = this.setDate(today);
    this.data.today = this.today;
  }
  @action
  setYesterday() {
    let yesterday = new Date();
    yesterday.setDate(yesterday.getDate() - 1);
    this.yesterday = this.setDate(yesterday.getTime());
  }
  @action
  lastTwo() {
    let type = 'get';
    let url = this.data.domain + '/search';
    let data =
      'type=lasttwodates&userid=' +
      this.data.userId +
      '&yesterday=' +
      this.yesterday;
    let processData = true;
    let contentType;
    this.data
      .ajax(type, url, data, processData, contentType, true)
      .then((response) => {
        this.lastTwoDates = JSON.parse(response);
        console.log(this.lastTwoDates);
      });
  }

  @action
  async searchDates(event) {
    let value = $(event.target).val();
    let start, end;
    console.log(value.length + 'el length' + value);
    let date = new Date();
    if (value.length == 2) {
      start = date.getFullYear() + '-' + value + '-' + '01';
      let lastDay = new Date(date.getFullYear(), value, 0);
      console.log(lastDay);
      end = date.getFullYear() + '-' + value + '-' + lastDay.getDate();
      console.log('start +' + start + 'end  ' + end);
    } else {
      start = value + '-01-01';
      end = value + '-12-31';
      console.log('start +' + start + 'end  ' + end);
    }
    let type = 'get';
    let url = this.data.domain + '/search?type=searchbetween';
    let data = 'userid=' + this.data.userId + '&start=' + start + '&end=' + end;
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
    this.data.dates = JSON.parse(response);
    let array = JSON.parse(response);
    this.data.searchList = A([]);
    array.forEach((data) => {
      let months = [
        'Jan',
        'Feb',
        'Mar',
        'Apr',
        'May',
        'Jun',
        'Jul',
        'Aug',
        'Sep',
        'Oct',
        'Nov',
        'Dec',
      ];
      let date = data.time.substring(0, 10);
      let text = data.text;
      let monthYear =
        months[Number(date.substring(5, 7)) - 1] + '-' + date.substring(0, 4);
      let day = date.substring(8, 10);

      let singleDay = {
        date,
        text,
        monthYear,
        day,
      };
      this.data.searchList.pushObject(singleDay);
    });
    this.router.transitionTo('writer.datelist');
    console.log('search list ended');
  }
  @action goTo(event) {
    let data = $(event.target).val();
    let date = this.setDate(data);
    this.router.transitionTo('writer.view', date);
  }
  @action
  show() {
    $('.filter').slideToggle();
  }
}
