import Component from '@glimmer/component';
import { action } from '@ember/object';
import { service } from '@ember/service';
export default class HeaderComponent extends Component {
  @service('data') data;
  toggled = false;
  @action
  didAction(event) {
    if (this.toggled) {
      $('.nav-bar').animate({
        marginLeft: '-278px',
      });

      $('.viewer').css('grid-template-columns', 'repeat(4,1fr)');
    } else {
      $('.nav-bar').animate({
        marginLeft: '0px',
      });
      $('.viewer').css('grid-template-columns', 'repeat(3,1fr)');
    }
    this.toggled = !this.toggled;
  }
}
