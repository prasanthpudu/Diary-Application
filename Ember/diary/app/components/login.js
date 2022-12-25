import Component from '@glimmer/component';
import { action } from '@ember/object';
import { service } from '@ember/service';
import Route from '@ember/routing/route';
export default class LoginComponent extends Component {
  @service('data') data;
  @service router;
  @action
  async login(event) {
    event.preventDefault();
    let userId = $('#userid').val();
    let type = 'post';
    let url = this.data.domain + '/login';
    let processData = true;
    let contentType;
    console.log(event);
    let action = $(event.target)[0].id;
    console.log(action);
    if (action == 'check') {
      let data = 'userid=' + userId + '&type=check';
      let response = await this.data.ajax(
        type,
        url,
        data,
        processData,
        contentType,
        true
      );
      console.log(response + 'from login');
      if (response == 'success') {
        console.log(response + 'from login');
        $('.module').animate(
          {
            width: 'toggle',
            opacity: 'toggle',
          },
          'fast'
        );
        // $(".password-form").slideDown();
      }
      if (response == 'notfound') {
        this.router.transitionTo('register');
      }
    }
    if (action == 'login') {
      let password = $('#password').val();
      let data = 'userid=' + userId + '&password=' + password + '&type=login';
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
        this.router.transitionTo('writer.view',this.data.today);
      } 
      else {
        $('.module').animate(
          {
            width: 'toggle',
            opacity: 'toggle',
          },
          'fast'
        );
        $('#login-info').text('Incorrect Username or  password');
      }
    }
  }
}
