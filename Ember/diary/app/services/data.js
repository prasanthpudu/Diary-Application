import Service from '@ember/service';
import { tracked } from '@glimmer/tracking';
import { A } from '@ember/array';
import { action } from '@ember/object';
export default class DataService extends Service {
  @tracked tempId;
  @tracked id;
  @tracked node;
  @tracked title;
  @tracked text;
  @tracked registered = true;
  @tracked searchList;
  @tracked userId;
  @tracked dates;
  @tracked notes = [];
  @tracked bioData;
  @tracked cropper = false;
  @tracked edit = false;
  @tracked newNote = false;
  @tracked profilePic;
  @tracked today;
  @tracked deletes=[];
  host = 'localhost:8080';
  domain = 'http://localhost:8080/Diary';
  domain1 = '/Diary';
  @action
  ajax(type, url, data, processData, contentType, withCredentials) {
    return $.ajax({
      type,
      url,
      data,
      xhrFields: {
        withCredentials,
      },
      processData,
      contentType,
      success: (response) => {
        console.log(response + 'responses');
      },
    });
  }
}
