import Component from '@glimmer/component';
import { service } from '@ember/service';
import { action } from '@ember/object';
import Route from '@ember/routing/route';
export default class RegisterComponent extends Component {
  @service('data') data;
  @service router;
  host = 'localhost:8080';
  @action
  check() {
    $.ajax({
      type: 'post',
      url: 'http://' + this.data.host + '/Diary/check',
      data: 'userid=' + this.userId,
      xhrFields: {
        withCredentials: true,
      },
      success: (response) => {
        console.log('resonponing');
        console.log(response);
        $('#userid-info').text('');
      },
      statusCode: {
        400: () => {
          $('#userid-info').text('Userid already taken');
        },
      },
    });
  }
  @action
  register() {
    if (
      this.validLocation &&
      this.validUserName &&
      this.dateOfBirth &&
      this.validPhoneNo
    ) {
      console.log('userid' + this.userId + 'password' + this.password);
      let params =
        'userid=' +
        this.data.userId +
        '&email=' +
        this.data.userId +
        '&password=' +
        this.data.password +
        '&username=' +
        this.userName +
        '&dateofbirth=' +
        this.dateOfBirth +
        '&location=' +
        this.location +
        '&gender=' +
        this.gender +
        '&phoneno=' +
        this.phoneNo;
      $.ajax({
        type: 'post',
        url: 'http://' + this.data.host + '/Diary/register',
        data: params,
        xhrFields: {
          withCredentials: true,
        },
        success: (response) => {
          console.log('resonponing');
          console.log(response);
          this.router.transitionTo('writer');
        },
        statusCode: {
          404: () => {
            console.log('Not found');
          },
          400: () => {
            this.router.transitionTo('login');
          },
        },
      });
    } else {
      $('#register-info').text('Enter valid credentials');
      setTimeout(() => {
        $('#register-info').text('');
      }, 3000);
    }
  }
  @action
  setGender(event) {
    this.gender = event.target.value;
  }
  @action
  checkName() {
    let string = this.userName;
    if (string.match('^[a-zA-Z]+$')) {
      $('#username-info').text('');
      this.validUserName = true;
    } else {
      $('#username-info').text('Invalid name');
      console.log('false');
      this.validUserName = false;
    }
  }
  @action
  checkLocation() {
    let string = this.location;
    if (string.match('^[a-zA-Z]+$')) {
      $('#location-info').text('');
      this.validLocation = true;
    } else {
      $('#location-info').text('Invalid location');
      console.log('false');
      this.validLocation = false;
    }
  }

  @action
  checkPhoneNo() {
    let string = this.phoneNo;
    if (string.match('\\d{5,12}')) {
      $('#phoneno-info').text('');
      this.validPhoneNo = true;
    } else {
      $('#phoneno-info').text('Invalid password');
      console.log('false');
      this.validPhoneNo = false;
    }
  }
}
