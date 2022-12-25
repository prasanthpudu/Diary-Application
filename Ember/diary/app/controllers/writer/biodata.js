import Controller from '@ember/controller';
import { action } from '@ember/object';
import { service } from '@ember/service';
import { tracked } from '@glimmer/tracking';
export default class WriterBiodataController extends Controller {
  @service('data') data;
  @tracked gender;
  @service router;
  @action
  async logout() {
    let type = 'get';
    let url = this.data.domain + '/session';
    let data = 'type=logout';
    let processData = true;
    let contentType;
    let response = await this.data.ajax(
      type,
      url,
      data,
      processData,
      contentType,
      true
    );
    if (response == 'success') {
      this.data.userId=null;
      this.router.transitionTo('login');
    }
  }
  @action
  setGender(event) {
    this.data.bioData.gender = event.target.value;
    console.log(event.target.value);
    console.log(this.data.bioData.gender);
  }
  @action
  assignGender() {
    $('#gender').val(this.data.bioData.gender).change();
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
    console.log('happening');
    let type = 'post';
    let url =
      this.data.domain +
      '/userdetails?type=updatebio&userid=' +
      this.data.userId;
    let data = JSON.stringify(json);
    let processData;
    let contentType;
    this.data.ajax(type, url, data, processData, false).then((response) => {
      console.log(this.data.bioData);
      $('.save-status').toggleClass('save-status-toggle');
      setTimeout(() => {
        $('.save-status').toggleClass('save-status-toggle');
      }, 2000);
    });
  }
}
