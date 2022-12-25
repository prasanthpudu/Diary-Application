import Component from '@glimmer/component';
import { action } from '@ember/object';
import { service } from '@ember/service';
import Route from '@ember/routing/route';
export default class AddNoteComponent extends Component {
  @service('data') data;
  @service router;
  @action
  async newNote() {
    $('#content-title').html('title');
    $('#content-text').html('text');
    $('.blur').css('z-index', '3');
    $('.outer').animate(
      {
        top: '45px',
      },
      'slow'
    );
    this.data.id="";
    this.data.edit = true;
    this.data.page = 'newnote';
  }
}
