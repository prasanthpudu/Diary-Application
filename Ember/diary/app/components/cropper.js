import Component from '@glimmer/component';
import { action } from '@ember/object';
import { service } from '@ember/service';
import { SHA256 } from 'crypto-js';
import Route from '@ember/routing/route';
export default class CropperComponent extends Component {
  @service router;
  @service('data') data;
  cropper;
  @action
  onLoad() {
    let image = document.getElementById('preview');
  }
  @action
  async didAction(event) {
    let fileName;
    let croppedImage = this.data.cropper
      .getCroppedCanvas()
      .toDataURL('image/png');
    console.log($(event.target));
    let id = $(event.target)[0].id;
    if ($(event.target)[0].id == 'crop') {
      $('.bio #profile-pic')[0].src = croppedImage;
      return;
    }
    switch (id) {
      case 'crop':
        $('.bio #profile-pic')[0].src = croppedImage;
        break;
      case 'upload':
        {

          await this.deleteProfile();
          this.data.cropper.getCroppedCanvas().toBlob(
            async (blob) => {
              let userId = $('#userid').val();
              let type = 'post';
              let url =
                this.data.domain +
                '/upload?action=upload&type=profile&userid=' +this.data.userId+"&oldfilename="+this.data.bioData.displayPicture;
                let date = new Date();
                
                fileName=date.toLocaleDateString() +
                date.toLocaleTimeString() +
                date.getMilliseconds();
                fileName = SHA256(fileName).toString();
              let processData = false;
              let contentType = false;
              const data = new FormData();
              data.append('croppedImage', blob, fileName);
              let response = await this.data.ajax(
                type,
                url,
                data,
                processData,
                contentType,
                true
              );
              this.data.bioData.displayPicture=fileName+".jpg";
              console.log(fileName);
              for (let i = 0; i < 1; i++) {
                this.data.profilePic =
                  'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png';
                await new Promise((r) => setTimeout(r, 200));
                this.data.profilePic =
                  this.data.domain +
                  '/assets/profile/' +fileName+'.jpg';
              }
            } /*, 'image/png' */
          );
          //console.log(croppedImage);
          
          this.data.cropper = false;
        }

        break;
    }
  }
  async deleteProfile(){
    let type = 'post';
    let url = this.data.domain + '/upload';
    let data = 'type=profile&action=delete&userid='+ this.data.userId+"&filename="+this.data.bioData.displayPicture;
    let processData = true;
    let contentType;
    this.data
      .ajax(type, url, data, processData, contentType, true)
      .then((response) => {
  
        this.data.profilePic =
        'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png';
      });

  }
  @action
  close() {
    this.data.cropper = false;
  }
}
