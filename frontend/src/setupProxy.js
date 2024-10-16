const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function(app) {
  app.use(
    "/api",
    createProxyMiddleware({
      target: "http://localhost:80/api/v1/greeting",
      pathRewrite: { "^/api": "" }
    })
  );
};
