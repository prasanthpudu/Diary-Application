import Component from '@glimmer/component';
import { service } from '@ember/service';

export default class ViewerComponent extends Component {
  @service('data') data;
}
