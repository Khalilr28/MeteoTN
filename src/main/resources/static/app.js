const $ = id => document.getElementById(id);

function describe(code, isDay) {
  if (code === 0)  return isDay ? ['☀️', 'Clear sky', 'clear'] : ['🌙', 'Clear night', 'night'];
  if (code <= 2)   return isDay ? ['⛅', 'Partly cloudy', 'cloud'] : ['☁️', 'Partly cloudy', 'night'];
  if (code === 3)  return ['☁️', 'Overcast', 'cloud'];
  if (code <= 48)  return ['🌫️', 'Foggy', 'fog'];
  if (code <= 57)  return ['🌦️', 'Drizzle', 'rain'];
  if (code <= 67)  return ['🌧️', 'Rain', 'rain'];
  if (code <= 77)  return ['❄️', 'Snow', 'snow'];
  if (code <= 82)  return ['🌧️', 'Rain showers', 'rain'];
  if (code <= 86)  return ['🌨️', 'Snow showers', 'snow'];
  return ['⛈️', 'Thunderstorm', 'storm'];
}

function el(tag, cls, text) {
  const e = document.createElement(tag);
  if (cls) e.className = cls;
  if (text !== undefined) e.textContent = text;
  return e;
}

function tick() {
  $('clock').textContent = new Date().toLocaleTimeString('en-GB',
    { timeZone: 'Africa/Tunis', hour: '2-digit', minute: '2-digit' }) + ' · Tunisia';
}

function render(city, data) {
  const c = data.current;
  const [icon, label, theme] = describe(c.weather_code, c.is_day);
  document.body.dataset.theme = theme;
  $('city').textContent = city;
  $('temp').textContent = Math.round(c.temperature_2m);
  $('desc').textContent = label;
  $('icon').textContent = icon;
  $('feels').textContent = Math.round(c.apparent_temperature) + '°C';
  $('hum').textContent = Math.round(c.relative_humidity_2m) + '%';
  $('wind').textContent = Math.round(c.wind_speed_10m) + ' km/h';

  const box = $('forecast');
  box.replaceChildren();
  data.daily.time.forEach((t, i) => {
    const d = el('div', 'glass day');
    const name = i === 0 ? 'Today'
      : new Date(t + 'T12:00').toLocaleDateString('en-GB', { weekday: 'short' });
    d.append(
      el('div', 'dn', name),
      el('div', 'di', describe(data.daily.weather_code[i], 1)[0]),
      el('div', 'hi', Math.round(data.daily.temperature_2m_max[i]) + '°'),
      el('div', 'lo', Math.round(data.daily.temperature_2m_min[i]) + '°')
    );
    box.append(d);
  });
}

async function loadCity(key, name) {
  document.querySelectorAll('.chip').forEach(b => b.classList.toggle('active', b.dataset.key === key));
  $('hero').classList.add('loading');
  $('stats').classList.add('loading');
  $('error').style.display = 'none';
  try {
    const res = await fetch('/api/weather/' + encodeURIComponent(key));
    if (!res.ok) throw new Error('HTTP ' + res.status);
    render(name, await res.json());
  } catch (e) {
    $('error').style.display = 'block';
  } finally {
    $('hero').classList.remove('loading');
    $('stats').classList.remove('loading');
  }
}

async function init() {
  tick();
  setInterval(tick, 30000);
  try {
    const cities = await (await fetch('/api/cities')).json();
    const nav = $('cities');
    cities.forEach(c => {
      const b = el('button', 'chip', c.name);
      b.dataset.key = c.key;
      b.addEventListener('click', () => loadCity(c.key, c.name));
      nav.append(b);
    });
    loadCity(cities[0].key, cities[0].name);
  } catch (e) {
    $('error').style.display = 'block';
  }
}
init();