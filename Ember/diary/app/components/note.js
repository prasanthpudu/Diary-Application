import Component from '@glimmer/component';
import { action } from '@ember/object';
import { service } from '@ember/service';
import Route from '@ember/routing/route';
export default class NoteComponent extends Component {
  @service('data') data;
  @service router;
  viewing = false;

 

  @action
  async option(event) {
    let data = $(event.target).parent();
    let userId = this.data.userId;
    let type = 'post';
    let url = this.data.domain + '/option';
    let processData = true;
    let contentType;
    console.log(data);
    let action = $(data)[0].id;
    let id = $($(data).parent()).parent()[0].id;
    console.log(action);
    if (action == 'option-star') {
      action = 'starnote';
      if (!id) {
        $(event.target).parent().toggleClass('star-note');
        return;
      }
      data = 'userid=' + userId + '&type=' + action + '&id=' + id;
      let response = await this.data.ajax(
        type,
        url,
        data,
        processData,
        contentType,
        true
      );
      if (response == 'success') {
        $(event.target).parent().toggleClass('star-note');
      }
    }
    if (action == 'option-delete') {
      action = 'delete';
      let type = 'post';
      let data = 'userid=' + userId + '&type=' + action + '&id=' + id;
      let url = this.data.domain + '/option';
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
      if(response.trim()=="success") {

      this.data.notes.forEach((note,index)=>{
        console.log(note);
        console.log(index);
        if(id==note.id){
        
          console.log("excuing replace");
          this.data.notes=(this.data.notes.removeAt(index));
         console.log(this.data.notes);
         return;
        }

      })
      
    }
  }
  }
  @action
  async didAction(event) {
    console.log(event);
    let action = $($(event.target).parent())[0].id;
    console.log(action);
    if (action === 'option-edit') {
      this.data.edit = true;
      $('.delete').ready(()=>{
        $('.delete').removeClass("hide-delete");
      })
      
    }
    if (action === 'option-view') {
      this.data.edit = false;
      $('.delete').ready(()=>{
        $('.delete').addClass("hide-delete");
      })
    }
    $('.blur').css('z-index', '3');
    $('.outer').animate(
      {
        top: '45px',
      },
      'slow'
    );

    $('#content-title').html(this.args.title);
    $('#content-text').html(this.args.text);
    this.data.id = this.args.id;
    this.data.noteDate = this.args.date;
    let media = this.args.media;
    let datas = $(this.setSrc(media));
    let element = document.createElement('p');
    for (let i = 0; i < datas.length; i++) {
      console.log($(datas[i])[0]);
      element.append($(datas[i])[0]);
    }

    let afterEvents=this.addEvents(element);
    $('#media').html(afterEvents);
   
   

  }

  @action addEvents(target){
    let elements = target.getElementsByClassName('files');
    let date = this.data.noteDate.substring(0,10);
    for (let i = 0; i < elements.length; i++) {

      let element = elements[i];
      console.log(element);
     //let hasIcon = element.lastChild.className=='delete'?true:false;
      if(date==this.data.today){
        console.log("dele eveve");
        var deleteIcon = document.createElement('button');
        deleteIcon.setAttribute('class','delete');
        deleteIcon.innerHTML="<span class=\"material-symbols-outlined\">delete</span>";
        addDeleteListener(deleteIcon,$(element)[0].id,this.data.deletes);
       element.appendChild(deleteIcon);
      }
      element.addEventListener('click', (event) => {


       
        $('.media-viewer').show();
        let tag = $(event.target).parent()[0].innerHTML;
        let tagName = $(tag).prop('tagName');
        console.log(tagName);
        tag = $(tag);
        if (tagName == 'VIDEO') {
          tag.attr('autoplay', 'true');
          tag.prop('controls', true);
        }
        if (tagName == 'SPAN') {
          console.log(event.target);
          tag = $($(tag)[1]);
          console.log(tag);
          tag.attr('autoplay', 'false');
          tag.prop('controls', true);
        }
        //document.getElementById("media-viewer").innerHTML=tag;
        $('.media-content').html(tag);
      });
    }


    function addDeleteListener(element,id,data){
      element.addEventListener("click", (e)=>{
        let classes = document.getElementById(id).className;
        let fileType=classes.substring(0,classes.indexOf(" "));
        let object= {
          type:fileType,
          fileName:id
        }
        data.push(object);
        $(element).parent().remove();
        e.stopPropagation();
      });
    }

    return elements;
  }

  @action
  setSrc(tags) {
    let date = this.data.noteDate.substring(0, 10);

    console.log("seting sr ---------------- from note");
    let elements = $(tags);
    console.log(elements);
    for (let i = 0; i < elements.length; i++) {
      let type = $(elements[i]).children().prop('tagName');
      let id = $(elements[i])[0].id;
      let url;
      if (type == 'IMG') {
        url =
          this.data.domain +
          '/assets/' +
          this.data.userId +
          '/media/image/' +
          date +
          '/' +
          id;
      }
      if (type == 'VIDEO') {
        url =
          this.data.domain +
          '/assets/' +
          this.data.userId +
          '/media/video/' +
          date +
          '/' +
          id;
      }
      console.log(type);
      if (type == 'SPAN') {
        url =
          this.data.domain +
          '/assets/' +
          this.data.userId +
          '/media/audio/' +
          date +
          '/' +
          id;
        $($(elements[i]).children()[1]).attr('src', url);
        console.log($($(elements[i]).children()[1]));
      }

      $(elements[i]).children().attr('src', url);
    }
    return elements;
  }
}

// let date = this.args.date.substring(0, 10);
      // type = 'get';
      // url = this.data.domain + '/search';
      // data =
      //   'type=getnotes&userid=' +
      //   this.data.userId +
      //   '&date=' +
      //   date +
      //   '&actiontype=view';

      // this.data
      //   .ajax(type, url, data, processData, contentType, true)
      //   .then((response) => {
      //     let json = JSON.parse(response);
      //     this.data.notes = json;
      //   });
      // console.log(response);
      // this.router.transitionTo('writer.viewer', date);
      // $('.save-status').toggleClass('save-status-toggle');
      // setTimeout(() => {
      //   $('.save-status').toggleClass('save-status-toggle');
      // }, 2000);