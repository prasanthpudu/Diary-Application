import Component from '@glimmer/component';
import { action } from '@ember/object';
import { service } from '@ember/service';

export default class NoteComponent extends Component {
  @service('data') data;
  @action
  getData(event) {
    let da = document.getElementById('note-content');
    let data = event.target;
    let note = $($(data).parent().children()[0]).children();
    let id = $(data).parent().attr('id');
    if (id == '') {
      id = null;
    }
    let title = $(note[0]).html();
    let text = $(note[1]).html();
    console.clear();
    console.log('title = ' + title + ' content= ' + text + ' id = ' + id);
    let json = {
      userId: this.data.userId,
      title,
      text,
      id,
    };
    $.ajax({
      type: 'post',
      url: 'http://' + this.data.host + '/Diary/edit',
      data: JSON.stringify(json),
      success: (response) => {
        console.log(response);
        $('.save-status').toggleClass('save-status-toggle');
        setTimeout(() => {
          $('.save-status').toggleClass('save-status-toggle');
        }, 2000);
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
  delete(event) {
    let data = event.target;
    console.log(data);
    let id = $($($($(data).parent()).parent()).parent())
      .parent()
      .attr('id');
    if (id == '') {
      id = null;
      return;
    }
    let params = 'userid=' + this.data.userId + '&id=' + id;
    $.ajax({
      type: 'get',
      url: 'http://' + this.data.host + '/Diary/deletenote',
      data: params,
      success: (response) => {
        console.log(response + 'responses');
        this.gets();
      },
    });
  }
  @action
  gets() {
    let date = this.args.date;
    console.log(date.substring(0, 10));
    console.log('date ' + date);
    let url = 'userid=' + this.data.userId + '&date=' + date + '&type=view';
    $.ajax({
      type: 'get',
      url: 'http://' + this.data.host + '/Diary/getnotes',
      data: url,
      success: (response) => {
        console.log(response);
        let json = JSON.parse(response);
        this.data.notes = json;
      },
    });
  }
  @action
  star(event) {
    let data = event.target;
    console.log(data);
    let id = $($($($(data).parent()).parent()).parent())
      .parent()
      .attr('id');
    if (id == '') {
      id = null;
      return;
    }
    let params = 'userid=' + this.data.userId + '&id=' + id;
    $.ajax({
      type: 'get',
      url: 'http://' + this.data.host + '/Diary/starnote',
      data: params,
      success: (response) => {
        console.log(response + 'responses');
        $(data).toggleClass('starednote');

      },
    });
  }
}
