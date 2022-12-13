import Controller from '@ember/controller';
import {action} from '@ember/object';
import {service} from '@ember/service';
export default class WriterStaredController extends Controller {
    @service('data') data;
    @action
  onLoad() {

    $.ajax({
      type: 'get',
      url: 'http://' + this.data.host + '/Diary/getstared',
      data: 'userid=' + this.data.userId,
      success: (response) => {
        let json = JSON.parse(response);
        console.log(response);
        this.data.notes = json;
        console.log('notes');
      },
      statusCode: {
        404: () => {
          console.log('no records');
        },
      },
    });
  }
}