<template>
  <b-container>
    <b-container class="col-md-12 justify-content-center">
      <b-form>
        <b-form-datepicker v-model="date"></b-form-datepicker>
        <b-button variant="primary" v-on:click="filterHikes()">Filter</b-button>
        <b-button variant="outline-primary" v-on:click="defilterHikes()">Clear filter</b-button>
      </b-form>
    </b-container>
    <b-card v-if="this.isLoggedIn" class="shadow-sm mb-5 rounded-0 border-0 col-md-3"
            img-top
            style="min-width: 18rem;max-width: 319px;"
    >
      <b-card-body class="justify-content-center">
        <b-button variant="link" class="text-decoration-none" v-on:click="()=>this.showHikeModal()">
          Add new hike
        </b-button>
      </b-card-body>
    </b-card>
    <b-card-group deck v-if="hikesUpdated" :key="key">
      <b-card v-for="hike in hikes" :key="hike.id" class="shadow-sm mb-5 rounded-0 border-0 col-md-3"
              img-top
              v-bind:img-src="'/api/image/'+hike.images[0].id"
              style="min-width: 18rem;max-width: 319px;"
      >
        <b-card-body>
          <p>
            Length: {{ hike.lengthInKm }} km
          </p>
          <b-button variant="link" class="text-decoration-none" :to="{name: 'hike-details', params: { id: hike.id }}">
            Details
          </b-button>
        </b-card-body>
      </b-card>
    </b-card-group>
    <b-modal id="addNewHikeModal" size="xl" v-model="this.showModal" v-on:hide="()=>this.onModalHide()"
             :hide-footer="true" :ok-disabled="true"
             :cancel-disabled="true">
      <b-row class="justify-content-center">
        <b-form>
          <b-form-group label="Length">
            <b-form-input v-model="newHikeLength" name="newHikeLength">
            </b-form-input>
          </b-form-group>
          <b-form-group label="Date">
            <b-form-datepicker v-model="newHikeStartDate" name="newHikeStartDate">
            </b-form-datepicker>
          </b-form-group>
          <b-form-group label="Coordinates">
            <b-form-input type="text" v-model="newHikeStartCoords" name="newHikeStartCoords">
            </b-form-input>
            <b-form-input type="text" v-model="newHikeEndCoords" name="newHikeEndCoords">
            </b-form-input>
          </b-form-group>
          <b-form-group label="Video upload">
            <b-form-group v-if="this.videoUploaded">
              <video v-if="this.videoUploaded" controls autoplay muted>
                <source v-if="this.videoUploaded" v-bind:src="'/api/video/'+this.newHikeVideoId" type="video/mp4">
              </video>
            </b-form-group>
            <b-form-file type="file" id="video" name="video" v-on:input="(file)=>this.uploadVideo(file)">
            </b-form-file>
          </b-form-group>
          <b-list-group v-if="this.imageUploaded" style="max-width: 500px; max-height: 600px;">
            <b-carousel
                id="carousel-1"
                v-model="slide"
                :interval="4000"
                controls
                indicators
                style="text-shadow: 1px 1px 2px #333;"
                @sliding-start="onSlideStart"
                @sliding-end="onSlideEnd"
            >
              <b-carousel-slide v-for="image in this.newHikeImageIds" v-bind:key="image"
                                v-bind:img-src="'/api/image/'+image"></b-carousel-slide>
            </b-carousel>
          </b-list-group>
          <b-form-group label="Photo upload">
            <b-form-file type="file" id="image" name="image" v-on:input="(file)=>this.uploadPhoto(file)">
            </b-form-file>
          </b-form-group>
          <b-form-row class="justify-content-end">
            <b-button variant="primary" v-on:click="()=>this.addNewHike()">Save</b-button>
          </b-form-row>
        </b-form>
      </b-row>
    </b-modal>
  </b-container>
</template>


<script>
export default {
  name: "Index",
  data() {
    return {
      hikes: [],
      isLoggedIn: false,
      isAdmin: false,
      newHikeLength: 0,
      newHikeStartDate: new Date(),
      newHikeImageIds: [],
      newHikeStartCoords: "",
      newHikeEndCoords: "",
      newHikeVideoId: null,
      showModal: false,
      videoUploaded: false,
      imageUploaded: false,
      hikesUpdated: true,
      date: null,
      key:0,
    };
  },
  beforeMount() {
    this.loadHikes();
    let user = JSON.parse(localStorage.getItem('hike-site.user'));
    if (user) {
      this.isLoggedIn = true;
      console.log(user);
    } else {
      this.isLoggedIn = false;
    }
  },
  methods: {
    defilterHikes() {
      if (this.date !== null) {
        this.date = null;
      }
      this.loadHikes();
    },
    filterHikes() {
      if (this.date !== null) {
        this.loadHikesByDate(this.date);
      }
    },
    loadHikes() {
      window.axios.get('/api/hike').then(res => {
        this.hikes = res.data;
        this.hikesUpdated = false;
        this.$nextTick(() => {
          // Add the component back in
          this.hikesUpdated = true;
          this.key+=1;
        });
      }).catch(() => console.log("error loading hike data"));
    },
    loadHikesByDate(date) {
      window.axios.get('/api/hike',{params:{ date: date}}).then(res => {
        this.hikes = res.data;
        this.hikesUpdated = false;
        this.$nextTick(() => {
          // Add the component back in
          this.hikesUpdated = true;
          this.key+=1;
        });
      }).catch(() => console.log("error loading hike data"));
    },
    uploadPhoto(file) {
      let formData = new FormData();
      formData.append("image", file);
      let headers = {'Content-Type': 'multipart/form-data'};
      window.axios.post('/api/image', formData, {headers}).then(response => {
        this.newHikeImageIds.push(response.data);
        this.imageUploaded = true;
      });
    },
    uploadVideo(file) {
      let formData = new FormData();
      formData.append("video", file);
      let headers = {'Content-Type': 'multipart/form-data'};
      window.axios.post('/api/video', formData, {headers}).then(response => {
        this.newHikeVideoId = response.data;
        this.videoUploaded = true;
      });
    },
    showHikeModal() {
      this.showModal = true;
    },
    onSlideStart(slide) {
      this.slide = slide;
      this.sliding = true
    },
    onSlideEnd(slide) {
      this.slide = slide;
      this.sliding = false
    },
    addNewHike() {
      window.axios.post('/api/hike/', {
        startDate: this.newHikeStartDate,
        lengthInKm: this.newHikeLength,
        startCoordinates: this.newHikeStartCoords,
        endCoordinates: this.newHikeEndCoords,
        imageIds: this.newHikeImageIds,
        videoId: this.newHikeVideoId
      }).then(() => {
        this.loadHikes();
        this.showModal = false;
        this.newHikeLength = 0;
        this.newHikeStartDate = new Date();
        this.newHikeImageIds = [];
        this.newHikeStartCoords = "";
        this.newHikeEndCoords = "";
        this.newHikeVideoId = null;
        this.showModal = false;
        this.videoUploaded = false;
        this.imageUploaded = false;
      });
    },
    onModalHide() {
      this.showModal = false;
    }
  },
}
</script>

<style scoped>

</style>
