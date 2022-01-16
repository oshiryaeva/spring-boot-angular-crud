function setupProxy({ tls }) {
  const conf = [
    {
      context: [
        '/api',
        '/services',
      ],
      target: `http${tls ? 's' : ''}://localhost:8080`,
      secure: false,
      changeOrigin: tls,
    },
  ];
  return conf;
}

module.exports = setupProxy;
