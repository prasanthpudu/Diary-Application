import Controller from '@ember/controller';
import { action } from '@ember/object';
import { service } from '@ember/service';
import { tracked } from '@glimmer/tracking';
export default class WriterBiodataController extends Controller {
  @service('data') data;
  @tracked gender;

  @action
  setGender(event) {
    this.gender = event.target.value;
  }
  @action
  update() {
    let json = {
      userId: this.data.userId,
      userName: this.data.bioData.name,
      location: this.data.bioData.location,
      dateOfBirth: this.data.bioData.dateofbirth,
      gender: this.data.bioData.gender,
      phoneno: this.data.bioData.phoneno,
    };
    $.ajax({
      type: 'post',
      url: 'http://' + this.data.host + '/Diary/updatebio',
      data: JSON.stringify(json),
      success: (response) => {
        console.log(response + 'responses');
        $('.save-status').toggleClass('save-status-toggle');
        setTimeout(() => {
          $('.save-status').toggleClass('save-status-toggle');
        }, 2000);
      },
    });
  }
}
