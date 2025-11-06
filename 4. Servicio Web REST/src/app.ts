import { Server } from './server.js';
import { AppRoutes } from './routes.js';
import { envs } from './config/envs.js';

(async () => {
  await main();
})();
async function main () {
  const server = new Server({
    port: envs.PORT,
    routes: AppRoutes.routes,
  });

  await server.start();
}