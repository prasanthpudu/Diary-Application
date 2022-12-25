import Component from '@glimmer/component';
import { action } from '@ember/object';
import { service } from '@ember/service';
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
          this.data.cropper.getCroppedCanvas().toBlob(
            async (blob) => {
              let userId = $('#userid').val();
              let type = 'post';
              let url =
                this.data.domain +
                '/upload?type=profile&userid=' +
                this.data.userId;
              let processData = false;
              let contentType = false;
              const data = new FormData();
              data.append('croppedImage', blob, this.data.userId);
              let response = await this.data.ajax(
                type,
                url,
                data,
                processData,
                contentType,
                true
              );
            } /*, 'image/png' */
          );
          //console.log(croppedImage);
          for (let i = 0; i < 2; i++) {
            this.data.profilePic =
              'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png';
            await new Promise((r) => setTimeout(r, 200));
            this.data.profilePic =
              this.data.domain +
              '/assets/profile/' +
              this.data.userId +
              '.jpg?time=' +
              new Date().getTime();
          }
          this.data.cropper = false;
        }

        break;
    }
  }
  @action
  close() {
    this.data.cropper = false;
  }
}
