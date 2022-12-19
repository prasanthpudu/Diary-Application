import Component from '@glimmer/component';
import { action } from '@ember/object';
import { service } from '@ember/service';
export default class CropperComponent extends Component {
  @service('data') data;
  cropper;
  @action
  onLoad() {
    let image = document.getElementById('preview');
  }
  @action
  didAction(event) {
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
              let url = this.data.domain + '/upload?type=profile';
              let processData = false;
              let contentType = false;
              const data = new FormData();
              formData.append('croppedImage', blob, this.data.userId);
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
        }
        break;
    }
  }
}
