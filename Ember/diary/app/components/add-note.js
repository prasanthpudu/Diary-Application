import Component from '@glimmer/component';
import { action } from '@ember/object';
import { service } from '@ember/service';
import Route from '@ember/routing/route';
export default class AddNoteComponent extends Component {
  @service('data') data;
  @service router;
  @action
  newNote() {
    console.log('newnorw');
    this.data.newNote= true;
    this.router.transitionTo('writer.view', "2022-12-19");
    let note = {
      title: '',
      text: '',
      starred: false,
      id: '',
    };
    console.log('executed newnote');
    this.data.notes = [note, ...this.data.notes];
  }
}
