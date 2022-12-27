import Component from '@glimmer/component';
import { action } from '@ember/object';
import { service } from '@ember/service';
import { SHA256 } from 'crypto-js';
import Route from '@ember/routing/route';
import {A} from '@ember/array';
export default class PageComponent extends Component {
  @service('data') data;
  @service router;
  videoIds=[];
  imageIds=[];
  audioIds=[];

  @action
  didAction(event) {
    console.log(event);
    let action = $($(event.target).parent())[0].id;
    console.log(action);
    if (action === 'page-close') {
      this.audioIds=[];
      this.videoIds=[];
      this.imageIds=[]; 
      this.data.deletes=[];
      setTimeout(() => {
        $('#media').text('');
      }, 500);
      $('.blur').css('z-index', '-1');
      $('.outer').animate(
        {
          top: '1000px',
        },
        'slow'
      );
      this.data.edit = false;
    }
  }
  @action delete(event){

  }
  @action
  async fileAction(event) {
    console.log(event);
    let element = $(event.target)[0];
    let id = $($(event.target).parent())[0].id;
    let files = element.files;
    let length = files.length;
    let data;
    if (id == 'button-video') {
      removeTags(this.videoIds);
      this.videoIds = [];
      for (let i = 0; i < length; i++) {
        let parent = document.createElement('div');
        await new Promise((r) => setTimeout(r, 1));
        let date = new Date();
        let tagId =
          date.toLocaleDateString() +
          date.toLocaleTimeString() +
          date.getMilliseconds();

        tagId = SHA256(tagId).toString();
        tagId += files[i].name.substring(files[i].name.lastIndexOf('.'));
        console.log(tagId);
        setAttributes(tagId, 'video', parent);
        this.videoIds.push(tagId);
        data = document.createElement('video');
        data.src = URL.createObjectURL(files[i]);
        $(parent).html(data);
        $('.media').append(parent);
      }
    }
    if (id == 'button-image') {
      removeTags(this.imageIds);
      this.imageIds = [];
      for (let i = 0; i < length; i++) {
        let parent = document.createElement('div');
        await new Promise((r) => setTimeout(r, 1));
        let date = new Date();
        let tagId =
        date.toLocaleDateString() +
        date.toLocaleTimeString() +
        date.getMilliseconds();
        tagId = SHA256(tagId).toString();
        tagId += files[i].name.substring(files[i].name.lastIndexOf('.'));
        setAttributes(tagId, 'image', parent);
        this.imageIds.push(tagId);
        data = document.createElement('img');
      
        data.src = URL.createObjectURL(files[i]);
        $(parent).html(data);
        $('#media').append(parent);
      }
    }
    if (id == 'button-audio') {
      removeTags(this.audioIds);
      this.audioIds = [];
      for (let i = 0; i < length; i++) {
        let parent = document.createElement('div');
        await new Promise((r) => setTimeout(r, 1));
        let date = new Date();
        let tagId =
          date.toLocaleDateString() +
          date.toLocaleTimeString() +
          date.getMilliseconds();
        tagId = SHA256(tagId).toString();
        tagId += files[i].name.substring(files[i].name.lastIndexOf('.'));
        console.log(tagId);
        setAttributes(tagId, 'audio', parent);

        this.audioIds.push(tagId);
        data = document.createElement('audio');
        data.src = URL.createObjectURL(files[i]);

        $(parent).append(
          '<span class="material-symbols-outlined">music_note</span>'
        );
        $(parent).append(data);
        $('.media').append(parent);
      }
    }
    function removeTags(tagIds) {
      let length = tagIds.length;
      console.log(length);
      console.log("child removed");
      for (let i = 0; i < length; i++) {
        console.log(tagIds[i]+"this is tag"+i+1);
        document.getElementById(tagIds[i]).remove();
      }
    }
   
    function setAttributes(id, className, element) {
      element.setAttribute('id', id);
      element.setAttribute('class', className + ' files');
      element.addEventListener('click', (event) => {
        $('.media-viewer').show();
        let tag = $(event.target).parent()[0].innerHTML;
        console.log(tag);
        let tagName = $(tag).prop('tagName');
        console.log(tagName);
        tag = $(tag);
        if (tagName == 'VIDEO') {
          tag.attr('autoplay', 'true');
          tag.prop('controls', true);
        }
        if (tagName == 'SPAN') {
          tag = $($(tag)[1]);
          console.log(tag);
          tag.attr('autoplay', 'false');
          tag.prop('controls', 'true');
        }
        //document.getElementById("media-viewer").innerHTML=tag;
        $('.media-content').html(tag);
      });
    }
  }
  @action
  async upload() {
    this.save();
    
    let videos = $('#video-input')[0].files;
    let audios = $('#audio-input')[0].files;
    let images = $('#image-input')[0].files;
    let formData = new FormData();
    formData = addFormData(images, formData, this.imageIds);
    formData = addFormData(videos, formData, this.videoIds);
    formData = addFormData(audios, formData, this.audioIds);
    console.log('formdata');
    console.log(formData);
    let type = 'post';
    let url =
      this.data.domain +
      '/upload?action=upload&type=media&date=' +
      this.data.today +
      '&userid=' +
      this.data.userId;
    let processData = false;
    let contentType = false;
    let  data = formData;
    let response = await this.data.ajax(
      type,
      url,
      data,
      processData,
      contentType,
      true
    );
    if(response.trim()=="success"){

    }
    url= this.data.domain+'/upload?action=delete&type=media&date='+this.data.today+"&userid="+this.data.userId;
    data= JSON.stringify(this.data.deletes);
     response = await this.data.ajax(
      type,
      url,
      data,
      processData,
      contentType,
      true
    );


    this.videoIds=[];
    this.imageIds=[];
    this.audioIds=[];
    function addFormData(files, formData, ids) {
      length = files.length;
      console.log(ids);
      for (let i = 0; i < length; i++) {
        console.log(files);
        let fileName = ids[i];
        console.log(fileName);
        formData.append('files', files[i], fileName);
      }
      return formData;
    }
    console.log(response);
  }
  @service('data') data;
  viewing = false;
  @action
  async save() {
    let temp;
    let data = $('.page .content');
    let note = $(data).children();
    let id = $(data).attr('id');
    if (id == '' ||id=='new') {
      if(id=='new'){
        temp=true;
      }
      id = null;
    }
    let title = $(note[0]).html();
    let text = $(note[1]).html();
    let mediaTag = document.getElementById("media");
    let elements = mediaTag.getElementsByClassName("files");
    for (let i = 0; i < elements.length; i++) {
      let deleteTag =document.getElementsByClassName("delete")[0];
      if(deleteTag){
        deleteTag.remove();
      }
    }
    let media = $(mediaTag).html();
    let type = 'post',
      url = this.data.domain + '/option?type=save',
      processData = true,
      contentType = false,
      json = {
        userId: this.data.userId,
        title,
        text,
        media,
        id,
      };
    data = JSON.stringify(json);
    let response = await this.data.ajax(
      type,
      url,
      data,
      processData,
      contentType,
      true
    );
    if (response != '[]') {
      if(id==null&&!temp){
        let note = JSON.parse(response);
       this.data.notes=[note,...this.data.notes];

      }
      else{
        if(temp){
          id="new";
        }
        this.data.notes.forEach((note,index)=>{
          console.log(note);
          console.log(index);
          if(id==note.id){
            let newNote = {
              text,
              title,
              media,
              id,
              time:note.time,
              stared:note.stared
            }
            
            console.log(newNote);
            console.log("excuing replace");
            this.data.notes=(this.data.notes.replace(index,1,[newNote]));
           console.log(this.data.notes);
           return;
          }
        })
      }
      $('.blur').css('z-index', '-1');
      $('.outer').animate(
        {
          top: '1000px',
        },
        'slow'
      );
      setTimeout(() => {
        $('#media').text('');
      }, 500);

      this.data.edit = false;
     
   
    }
  }
}














// if (this.data.page == 'stared') {
//   let type = 'get';
//   let url = this.data.domain + '/search';
//   let data = 'type=stared&userid=' + this.data.userId;
//   let processData = true;
//   let contentType;
//   this.data
//     .ajax(type, url, data, processData, contentType, true)
//     .then((response) => {
//       this.data.notes = JSON.parse(response);
//     });
//   return;
// }



// type = 'get';
// url = this.data.domain + '/search';
// data =
//   'type=getnotes&userid=' +
//   this.data.userId +
//   '&date=' +
//   this.data.today +
//   '&actiontype=view';
// processData = true;
// contentType;
// this.data
//   .ajax(type, url, data, processData, contentType, true)
//   .then((response) => {
//     let json = JSON.parse(response);
//     this.data.notes = json;
//     console.log(this.data.newNote);
//   });
// this.router.transitionTo('writer.view', this.data.today);
// console.log(response);
// $('.save-status').toggleClass('save-status-toggle');
// setTimeout(() => {
//   $('.save-status').toggleClass('save-status-toggle');
// }, 2000);