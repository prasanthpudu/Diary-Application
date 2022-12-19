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
}
