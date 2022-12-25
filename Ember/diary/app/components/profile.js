import Component from '@glimmer/component';
import { service } from '@ember/service';
import { action } from '@ember/object';
export default class ProfileComponent extends Component {
  @service('data') data;
  @action
  didAction(event) {
    var tgt = event.target;
    let files = tgt.files;

    // FileReader support
    if (FileReader && files && files.length) {
      var fr = new FileReader();
      fr.onload = () => {
        this.data.cropper = true;
        setTimeout(() => {
          let image = document.getElementById('preview');
          image.src = fr.result;
          this.data.cropper = new Cropper(image, {
            viewMode: 1,
            aspectRatio: 1,
          });
          console.log(this.cropper);
        }, 200);
      };
      fr.readAsDataURL(files[0]);
    }
  }
  @action
  getBioData() {
    console.log('happening');
    let type = 'post';
    let url = this.data.domain + '/userdetails';
    let data = 'type=getbio&userid=' + this.data.userId;
    let processData = true;
    let contentType;
    this.data
      .ajax(type, url, data, processData, contentType, true)
      .then((response) => {
        this.data.bioData = JSON.parse(response);
        console.log(this.data.bioData);
        this.setProfilePic();
      });
  }
  @action
  setProfilePic() {
    if (this.data.bioData.displayPicture) {
      this.data.profilePic =
        this.data.domain + '/assets/profile/' + this.data.userId + '.jpg';
      return;
    }
    this.data.profilePic =
      'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png';
  }
}
