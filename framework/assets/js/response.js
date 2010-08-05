/**
 * This class is for getting responses and delegating them
 * to where they should go
 * @constructor
 */

function Response() {
  this.port = window.DroidGap.getPort();
}

Response.prototype.checkCmd = function()
{
  console.log('checking command');
  xhr = new XMLHttpRequest();
  xhr.open("GET", "http://localhost:" + this.port, true);
  resp = JSON.parse(xhr.responseText)
  switch(resp.type)
  {
    case "accel":
      navigator.accelerometer.gotCurrentAcceleration(resp.key, resp.x, resp.y, resp.z);
      console.log('accel');
      break;
    case "geo" : 
    break;
    case "camera" :
    break;
    case "compass":
    break;
  }
}

PhoneGap.addConstructor(function() {
  if (typeof navigator.response == "undefined")
  {
    navigator.response = new Response();
    setTimeout(navigator.response.checkCmd, 500);
  }
});
