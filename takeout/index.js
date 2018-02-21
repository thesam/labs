const Nightmare = require('nightmare')
const nightmare = Nightmare({ show: true, openDevTools: true})

const EMAIL = process.env.TAKEOUT_EMAIL || 'foo@gmail.com';
const PW = process.env.TAKEOUT_PW || 'bar';

nightmare
  .goto('https://takeout.google.com/settings/takeout')
  .wait('#Email')
  .type('#Email', EMAIL)
  .click('#next')
  .wait('#Passwd')
  .type('#Passwd', PW)
  .wait('#signIn')
  .click('#signIn')
  // .wait('#r1-0 a.result__a')
  // .evaluate(() => document.querySelector('#r1-0 a.result__a').href)
  //.end()
  .then(console.log)
  // .catch(error => {
  //   console.error('Search failed:', error)
  // })
