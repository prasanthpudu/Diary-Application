import Controller from '@ember/controller';
import { action } from '@ember/object';
import { service } from '@ember/service';
import Route from '@ember/routing/route';
export default class ForgotpasswordController extends Controller {
  @service router;
  @service('data') data;
  @action
  async forgot(event) {
    event.preventDefault();
    if (!this.validPassword) {
      return;
    }
    console.log('executing');
    let securityQuestion = this.question1 + this.question2;
    let type = 'post';
    let url = this.data.domain + '/userdetails';
    let processData = true;
    let contentType;
    let data =
      'userid=' +
      this.data.tempId +
      '&type=changepassword&securityquestion=' +
      securityQuestion +
      '&newpassword=' +
      this.password;
    let response = await this.data.ajax(
      type,
      url,
      data,
      processData,
      contentType,
      true
    );
    if (response.trim() === 'success') {
      this.router.transitionTo('login');
      await new Promise((r) => setTimeout(r, 100));
      $('#login-info').text('Password changed successfully');
      $('#login-info').css('color', 'green');
    } else {
      this.router.transitionTo('login');
      await new Promise((r) => setTimeout(r, 100));
      $('#login-info').text('Incorrect Security answers');
      $('#login-info').css('color', 'red');
    }
    console.log(response + 'from login');
  }
  @action
  checkPassword() {
    let string = this.password;
    if (string.match('^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$')) {
      $('#password-info').text('');
      this.validPassword = true;
    } else {
      $('#password-info').text('Invalid password');
      console.log('false');
      this.validPassword = false;
    }
  }
}
