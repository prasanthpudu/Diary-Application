import Component from '@glimmer/component';
import { service } from '@ember/service';
import { action } from '@ember/object';
import Route from '@ember/routing/route';
export default class RegisterComponent extends Component {
  @service('data') data;
  @service router;
  host = 'localhost:8080';
  @action
  next(event){
    event.preventDefault();
    $(".register-form").hide();
    $(".security").show();
    console.log('executed')
  }
  @action setGender(event){
    this.gender=event.target.value;
  }
  @action
  async register(event) {
    {
      event.preventDefault();
      let form = $(event.target).parent();
      console.log(form);
      if (form[0].reportValidity()) {
        // will evaluate to TRUE if all ok else FALSE and also show validation messages
        console.log('validated');
        console.log('userid' + this.userId + 'password' + this.password);
        let type = 'post';
        let url = this.data.domain + '/register';
        let processData = true;
        let contentType;
        let data =
          'email=' +
          this.data.userId +
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
          '&securityquestion='+this.question1+this.question2+
          '&type=register';
        let response = await this.data.ajax(
          type,
          url,
          data,
          processData,
          contentType,
          true
        );
        if (response == 'success') {
          this.router.transitionTo('writer');
        } else {
          this.router.transitionTo('login');
        }
      }
    }
  }
}
