import Component from '@glimmer/component';
import { action } from '@ember/object';
export default class MediaViewerComponent extends Component {
  @action
  didAction(event) {
    console.log(event);

    $('.media-viewer').hide();
    $('.media-content').html('');
  }
}
