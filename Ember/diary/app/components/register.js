import Component from '@glimmer/component';
import { service } from '@ember/service';
import { action } from '@ember/object';
import Route from '@ember/routing/route';
export default class RegisterComponent extends Component {
  @service('data') data;
  @service router;
  host = 'localhost:8080';
  @action
  next(event) {
    event.preventDefault();
    if (
      !this.validLocation ||
      !this.validUserName ||
      !this.validDateOfBirth ||
      !this.validPhoneNo ||
      !this.validPassword
    ) {
      $('#register-info').text('Please enter a valid details');
      return;
    }
    let form = $(event.target).parent();
    if (form[0].reportValidity()) {
      $('.register-form').hide();
      $('.security').show();
      console.log('executed');
    }
  }
  @action setGender(event) {
    this.gender = event.target.value;
  }
  @action
  async register(event) {
    {
      event.preventDefault();
      let form = $(event.target).parent();
      console.log(form);
      if (form[0].reportValidity()) {
        console.log('validated');
        console.log('userid' + this.userId + 'password' + this.password);
        let type = 'post';
        let url = this.data.domain + '/register';
        let processData = true;
        let contentType;
        let data =
          'email=' +
          this.data.tempId +
          '&password=' +
          this.password +
          '&username=' +
          this.userName +
          '&dateofbirth=' +
          this.dateOfBirth +
          '&location=' +
          this.location +
          '&gender=' +
          this.gender +
          '&phoneno=' +
          this.phoneNo +
          '&securityquestion=' +
          this.question1 +
          this.question2 +
          '&type=register';
        let response = await this.data.ajax(
          type,
          url,
          data,
          processData,
          contentType,
          true
        );
        this.data.userId = JSON.parse(response).userId;
        if (this.data.userId) {
         
          this.router.transitionTo('writer');
        } else {
          this.router.transitionTo('login');
          await new Promise((r) => setTimeout(r, 100));
          $('#login-info').text('Registration Failed');
        }
      }
    }
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
    if (string.match('^\\d{5,12}$')) {
      $('#phoneno-info').text('');
      this.validPhoneNo = true;
    } else {
      $('#phoneno-info').text('Invalid phone number');
      console.log('false');
      this.validPhoneNo = false;
    }
  }
  @action
  checkPassword() {
    let string = this.password;
    if (string.match('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$.])[a-zA-Z\\d@$.]{8,}$')) {
      $('#password-info').text('');
      this.validPassword = true;
    } else {
      $('#password-info').text('Invalid password');
      console.log('false');
      this.validPassword = false;
    }
  }
  @action
  checkDob() {
    let string = this.dateOfBirth;
    let inputdate = new Date(string);
    let currentDate = new Date();
    if (inputdate < currentDate) {
      $('#date-info').text('');
      this.validDateOfBirth = true;
    } else {
      $('#date-info').text('Invalid Date Of Birth');
      console.log('false');
      this.validDateOfBirth = false;
    }
  }
}
