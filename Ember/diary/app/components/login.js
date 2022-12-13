import Component from '@glimmer/component';
import { action } from '@ember/object';
import { service } from '@ember/service';
import Route from '@ember/routing/route';
export default class LoginComponent extends Component {
  @service('data') data;
  @service router;
  @action
  login() {
    if (!this.validPassword) {
      return;
    }
    $.ajax({
      type: 'post',
      url: 'http://' + this.data.host + '/Diary/login',
      data: 'userid=' + this.data.userId + '&password=' + this.data.password,
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
          console.log('not found');
          this.router.transitionTo('register');
        },
        400: () => {
          $('#login-info').text('incorrect username or password');
          setTimeout(() => {
            $('#login-info').text('');
          }, 3000);
        },
      },
    });
  }
  @action
  checkPassword() {
    let string = this.data.password;
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
